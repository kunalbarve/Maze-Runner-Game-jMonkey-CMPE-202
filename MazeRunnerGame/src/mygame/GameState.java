package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.ChaseCamera;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.SkyFactory;

public class GameState extends StateManager implements ActionListener, PhysicsCollisionListener {
    
    private AnimChannel ach;
    private AnimControl act;
    private Spatial sceneModel;
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private CharacterControl player;
    private PhysicsCharacter firstPlayer;
    private Node player_node; // Used to fix the model Y offset
    private Node player_model;
    private ChaseCamera chase_cam; // Third person camera
    protected FlyByCamera flyCam;
    AudioNode music;
    protected Camera cam;
    private ViewPort viewPort;
    private Node localrootNode;
    private AssetManager assetManager;
    private TimerUtil timerUtil;
    private Counter counter;
    protected InputManager inputManager;
    
    protected AbstractSimpleApplication app2;
    
    private Vector3f walk_direction = new Vector3f();
    private boolean left = false, right = false, up = false, down = false,
                    attacking = false, running = false, lock_movement = false, isInThirdPerson= true;   
    
    private Vector3f direction = new Vector3f();
    private Vector3f left_direction = new Vector3f();
    
    private int coinCount = 0;
    private boolean isCoinCollected = false;
    
    public GameState(AbstractSimpleApplication app){
        this.viewPort      = app.getViewPort();
        this.assetManager  = app.getAssetManager();  
        this.cam = app.getCamera();
        this.inputManager = app.getInputManager();
        app2 = app;
        localrootNode = new Node();
        flyCam = new FlyByCamera(cam);
  }
    
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        /** Load this scene */
          bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        init_models();
        init_physics(new Vector3f(60, 10, -60));
        initSky();
        init_camera();
        init_keys();
        init_light();
        init_anim();
        playMusic();
        init_coins();
        
        timerUtil = new TimerUtil(app2, 18000);
        ConcreteObserver timeObserver = new ConcreteObserver(timerUtil);
        timerUtil.add(timeObserver);
        timerUtil.setTimerOnScreen();
        
        counter = new Counter(app2);
        ConcreteObserver countObserver = new ConcreteObserver(counter);
        counter.add(countObserver);
        counter.setScoreScreen();
        
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }
     
    public void init_thirdPerson(Vector3f location){
    // Third parameter of CapsuleCollisionShape is the axis, 1 = Y
        CapsuleCollisionShape capsule_shape =
            new CapsuleCollisionShape(2.3f, 2.6f, 1);
        player = new CharacterControl(capsule_shape, .05f);
            // 0.05f is the highest step you can climb without jumping
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(100);
        player_node.addControl(player);
        player.setPhysicsLocation(location);
        player_node.setLocalTranslation(location);
        localrootNode.attachChild(player_node);
        bulletAppState.getPhysicsSpace().add(player);
    }
    
    public void init_firstPerson(Vector3f location){
        firstPlayer = new PhysicsCharacter(new SphereCollisionShape(5), .01f);
        firstPlayer.setJumpSpeed(20);
        firstPlayer.setFallSpeed(30);
        firstPlayer.setGravity(30);
        firstPlayer.setPhysicsLocation(location);
        bulletAppState.getPhysicsSpace().add(firstPlayer);
    }
    
    public Sphere getCoinSphere(){
        Sphere box = new Sphere(12, 10, 0.7f, true, false);
        box.setTextureMode(Sphere.TextureMode.Projected);
       return box;
    }
    
    public Geometry getCoin(Vector3f coinLocation){
        Geometry boxGeometry = new Geometry("Box", getCoinSphere());
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        
        material.setColor("Color", ColorRGBA.Yellow);

        boxGeometry.setLocalTranslation(coinLocation);
        boxGeometry.setMaterial(material);

        //RigidBodyControl automatically uses Sphere collision shapes when attached to single geometry with sphere mesh
        boxGeometry.addControl(new RigidBodyControl(.000f));
        boxGeometry.getControl(RigidBodyControl.class).setRestitution(1);
        
        return boxGeometry;
    }
    
    public void init_coins(){
       Geometry boxGeometry1 = getCoin(new Vector3f(80, 8, -20));
       Geometry boxGeometry2 = getCoin(new Vector3f(10, 11, 0));
       Geometry boxGeometry3 = getCoin(new Vector3f(-20, 4, 180));
       Geometry boxGeometry4 = getCoin(new Vector3f(-130, 3, 95));
       Geometry boxGeometry5 = getCoin(new Vector3f(-10, 25, 20));
       Geometry boxGeometry6 = getCoin(new Vector3f(-115, 5, 53));
       Geometry boxGeometry7 = getCoin(new Vector3f(100, 3, -70));
        
       localrootNode.attachChild(boxGeometry1);
        bulletAppState.getPhysicsSpace().add(boxGeometry1);
        
        localrootNode.attachChild(boxGeometry2);
        bulletAppState.getPhysicsSpace().add(boxGeometry2);
        
        localrootNode.attachChild(boxGeometry3);
        bulletAppState.getPhysicsSpace().add(boxGeometry3);
   
        localrootNode.attachChild(boxGeometry4);
        bulletAppState.getPhysicsSpace().add(boxGeometry4);
        
        localrootNode.attachChild(boxGeometry5);
        bulletAppState.getPhysicsSpace().add(boxGeometry5);
        
        localrootNode.attachChild(boxGeometry6);
        bulletAppState.getPhysicsSpace().add(boxGeometry6);
        
        localrootNode.attachChild(boxGeometry7);
        bulletAppState.getPhysicsSpace().add(boxGeometry7);
    }
    
    // Physics -------------------------------------------------------
    private void init_physics(Vector3f location) {
        //terrain.addControl(new RigidBodyControl(0));
         // Our rigid scene
        CollisionShape scene_shape = CollisionShapeFactory.createMeshShape((Node)sceneModel);
        landscape = new RigidBodyControl(scene_shape, 0); // Static physics node with mass 0
        sceneModel.addControl(landscape);
        
        if(isInThirdPerson){
            init_thirdPerson(location);
        }else{
            init_firstPerson(location);
        }
        
         // Physic nodes are attached to the physics space
        // instead of the root node
        bulletAppState.getPhysicsSpace().add(landscape);
        localrootNode.attachChild(sceneModel);
     }
    
    // Models --------------------------------------------------------
    private void init_models() {
        player_node = new Node("ThirdPerson");
        player_model = (Node)assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");
        player_model.setLocalScale(0.035f);
        player_model.getLocalTranslation().addLocal(0, -3.6f, 0); // model offset fix
        player_node.attachChild(player_model);
        
        sceneModel = assetManager.loadModel("Scenes/try.j3o");
        sceneModel.setLocalScale(2f);
    }
    
    void init_camera() {
        viewPort.setBackgroundColor(new ColorRGBA(0.7f,0.8f,1f,1));
        flyCam.setEnabled(false);
        flyCam.setMoveSpeed(100);
        this.cam.setFrustumFar(2000);
        chase_cam = new ChaseCamera(cam, player_model, inputManager);
        chase_cam.setDragToRotate(false);
        chase_cam.setInvertVerticalAxis(true);
        chase_cam.setLookAtOffset(new Vector3f(0, 2f, 0));
    }
    
    public void setAttackAnimation(){
        if (!ach.getAnimationName().equals("Attack3")) {
            ach.setAnim("Attack3");
            ach.setSpeed(1f);
            ach.setLoopMode(LoopMode.Loop);
        }
    }
    
    public void setSteadyWalkAnimation(){
        if (!ach.getAnimationName().equals("Idle2")) {
            ach.setAnim("Idle2", 0f);
            ach.setSpeed(1f);
            ach.setLoopMode(LoopMode.Loop);
         }
    }
    
    public void setWalkAnimation(){
        if(!ach.getAnimationName().equals("Walk")){
            ach.setAnim("Walk", 0.5f);
            ach.setLoopMode(LoopMode.Loop);
        }
    }
    
    @Override
    public void update(float k) {
        
        increaseCoinCount();
        timerUtil.updateTime();
        float movement_amount = 0.5f;
        if(running) movement_amount *= 2.75;
        
        // Gets forward direction and moves it forward
        direction = cam.getDirection().clone().multLocal(movement_amount);
        // Gets left direction and moves it to the left
        left_direction = cam.getLeft().clone().multLocal(movement_amount * 0.75f);
        
        
        // We don't want to fly or go underground
        direction.y = 0;
        left_direction.y = 0;
       walk_direction.set(0, 0, 0); // The walk direction is initially null

        if(left) 
            walk_direction.addLocal(left_direction);
        if(right) 
            walk_direction.addLocal(left_direction.negate());
        if(up) 
            walk_direction.addLocal(direction);
        if(down) 
            walk_direction.addLocal(direction.negate());
        
        if(isInThirdPerson){
            
            if(attacking) {
                lock_movement = true;
                setAttackAnimation();
            } else {
                // If we're not walking, set standing animation if not jumping
                if (walk_direction.length() == 0) {
                     setSteadyWalkAnimation();
                } else {
                    // ... otherwise, set the walking animation
                    setWalkAnimation();
                    
                    if(running) ach.setSpeed(1.75f);
                    else ach.setSpeed(1f);
                }
            }
            if(!lock_movement)
                player.setWalkDirection(walk_direction);
            else
                player.setWalkDirection(Vector3f.ZERO);

            // Rotate model to point walk direction if moving
            if(walk_direction.length() != 0)
            player.setViewDirection(walk_direction.negate());
        }else{
            firstPlayer.setWalkDirection(walk_direction);
            cam.setLocation(firstPlayer.getPhysicsLocation());
        }
 }
    
    // Animations ----------------------------------------------------
    private void init_anim() {
        act = player_model.getControl(AnimControl.class);
        //act.addListener(this);
        ach = act.createChannel();
        ach.setAnim("Idle2");
    }
    
    public void onAnimCycleDone(AnimControl ctrl, AnimChannel ch, String name) {
        if(name.equals("Attack3") && attacking) {
            if (!ch.getAnimationName().equals("Idle2")) {
                ch.setAnim("Idle2", 0f);
                ch.setLoopMode(LoopMode.Loop);
                ch.setSpeed(1f);
                attacking = false;
                lock_movement = false;
            }
        }
    }
    
   // Light ---------------------------------------------------------
    private void init_light() {
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f)); // mult makes it brighter
        localrootNode.addLight(al);
 
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White); 
        sun.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        localrootNode.addLight(sun);        
    }
    
    private void initSky(){
        localrootNode.attachChild(SkyFactory.createSky(
            assetManager, "Textures/Sky/sky.jpg", true));
    }

    private void playMusic() {
        music = new AudioNode(assetManager, "Sounds/sample.ogg", true);
        
        music.setPositional(false);
        music.play();
    }
    
    /** We over-write some navigational key mappings here, so we can
    add physics-controlled walking and jumping: */
    
    // Controls ------------------------------------------------------
    private void init_keys() {
        inputManager.addMapping("Left",  new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up",    new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down",  new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("FirstPerson",    new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("ThirdPerson",  new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("Jump",  new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Run",    new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addMapping("Attack", new KeyTrigger(KeyInput.KEY_X));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "FirstPerson");
        inputManager.addListener(this, "ThirdPerson");
        inputManager.addListener(this, "Jump");
        inputManager.addListener(this, "Run");
        inputManager.addListener(this, "Attack");
    }
    
    /** These are our custom actions triggered by key presses.
    We do not walk yet, we just keep track of the direction the user pressed. */
    public void onAction(String name, boolean pressed, float k) {
        if (name.equals("Left"))
            left = pressed;
        else if (name.equals("Right"))
            right = pressed;
        else if (name.equals("Up"))
            up = pressed;
        else if (name.equals("Down"))
            down = pressed;
        else if (name.equals("FirstPerson") && isInThirdPerson){
            isInThirdPerson = false;
            toggleView("StartFirstPerson");
        }else if (name.equals("ThirdPerson") && !isInThirdPerson){
            isInThirdPerson = true;
            toggleView("StartThirdPerson");
        }
        else if (name.equals("Jump")){
            if(isInThirdPerson)
                player.jump();
            else
                firstPlayer.jump();
        }else if (name.equals("Run"))
            running = pressed;
        else if (name.equals("Attack") && isInThirdPerson){
                if(pressed && !attacking)
                    attacking = true;
                else{
                    attacking = false;
                    lock_movement = false;
                }
        }
    }

    private void toggleView(String view) {
        if(view.equalsIgnoreCase("StartFirstPerson")){
            Vector3f location = player.getPhysicsLocation();
            bulletAppState.getPhysicsSpace().remove(player);
            localrootNode.detachChild(player_node);
            init_physics(location);
        }else if(view.equalsIgnoreCase("StartThirdPerson")){
            Vector3f location = firstPlayer.getPhysicsLocation();
            bulletAppState.getPhysicsSpace().remove(firstPlayer);
            init_physics(location);
        }
    }
    
    public void collision(PhysicsCollisionEvent event) {
        String s1 = "ThirdPerson";
        String s2 = "Box";
        if(event.getNodeA() != null && event.getNodeB() != null){
            if(s1.equalsIgnoreCase(event.getNodeA().getName()) && s2.equalsIgnoreCase(event.getNodeB().getName())){
                 PhysicsCollisionObject objectToRemove=event.getObjectB();
                 Spatial n=event.getNodeB();
                 n.removeFromParent();
                 bulletAppState.getPhysicsSpace().removeCollisionObject(objectToRemove);
                 localrootNode.updateGeometricState();
                 isCoinCollected = true;
            }else if(s2.equalsIgnoreCase(event.getNodeA().getName()) && s1.equalsIgnoreCase(event.getNodeB().getName())){
                 PhysicsCollisionObject objectToRemove=event.getObjectA();
                 Spatial n=event.getNodeA();
                 n.removeFromParent();
                 bulletAppState.getPhysicsSpace().removeCollisionObject(objectToRemove);
                 localrootNode.updateGeometricState();
                 isCoinCollected = true;
            }
        }
    }
    
    @Override
    public void stateAttached(AppStateManager stateManager) {
      app2.getRootNode().attachChild(localrootNode);
    }
    
    public void detachAllLights(){
        for (Object object : localrootNode.getLocalLightList()) {
            localrootNode.removeLight((Light) object);
        }
    }
 
    @Override
    public void stateDetached(AppStateManager stateManager) {
      localrootNode.detachAllChildren();
      detachAllLights();
      music.stop();
      app2.getRootNode().detachChild(localrootNode);
      app2.getGuiNode().detachAllChildren();
   }

    private void increaseCoinCount() {
        if(isCoinCollected){
            coinCount++;
            counter.setCoinCount(coinCount);
            counter.displayScore();
            isCoinCollected = false;
        }
    }
    
    public void setCoinCount(int count){
        coinCount = count;
    }

    @Override
    int getCurrent() {
        // No Implementation
        return  0;
    }
}