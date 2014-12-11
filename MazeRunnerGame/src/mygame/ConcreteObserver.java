package mygame;

public class ConcreteObserver implements Observer {
 
	protected TimerUtil timerSubject;
        protected Counter counterSubject;
        protected  AbstractSimpleApplication app;

    public ConcreteObserver( TimerUtil theSubject )
    {
        this.timerSubject = theSubject ;
    }
    
    public ConcreteObserver( Counter theSubject )
    {
        this.counterSubject = theSubject ;
    }
    
    public void updateBasedOnTimer(AbstractSimpleApplication app) {
        if(timerSubject.getTimerSeconds() == 0){
            app.detachMainGame();
        }
    }

    public void updatebasedOnCounter(AbstractSimpleApplication app) {
        if(counterSubject.getCoinCount() == 7){
            app.detachMainGame();
        }
    }
}