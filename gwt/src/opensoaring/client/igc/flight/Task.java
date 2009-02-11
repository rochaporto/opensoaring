package opensoaring.client.igc.flight;

import java.util.ArrayList;

public class Task {

	private ArrayList<TaskLeg> taskLegs = new ArrayList<TaskLeg>();
	
	public Task(int numLegs) {
		for (int i=0; i<numLegs; i++) {
			taskLegs.add(new TaskLeg());
		}
	}
	
	public Task(ArrayList<TaskLeg> taskLegs) {
		this.taskLegs = taskLegs;
	}

	public double getDistance() {
		double distance = 0;
		for (TaskLeg leg: taskLegs) {
			distance += leg.getDistance();
		}
		return distance;
	}
	
	/**
	 * @return the taskLegs
	 */
	public ArrayList<TaskLeg> getTaskLegs() {
		return taskLegs;
	}

	/**
	 * @param taskLegs the taskLegs to set
	 */
	public void setTaskLegs(ArrayList<TaskLeg> taskLegs) {
		this.taskLegs = taskLegs;
	}
	
	public void addTaskLeg(TaskLeg taskLeg) {
		taskLegs.add(taskLeg);
	}
	
	public void setTaskLeg(int index, TaskLeg taskLeg) {
		taskLegs.set(index, taskLeg);
	}
}
