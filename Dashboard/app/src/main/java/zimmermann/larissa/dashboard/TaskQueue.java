package zimmermann.larissa.dashboard;

import java.util.List;

/**
 * Created by laris on 24/10/2017.
 */

public class TaskQueue {
    private List<Task> queue;

    public TaskQueue() {
    }

    public void push(Task newTask) {
        this.queue.add(newTask);
    }

    public void pop(){
        //enviar para processar

    }
}
