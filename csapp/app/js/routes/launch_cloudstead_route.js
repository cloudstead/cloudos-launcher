App.LaunchCloudsteadRoute = App.ProtectedRoute.extend({
	taskId: "",

	model: function(params) {
		this.set("taskId", params.task_id);
		return API.get_task(params.task_id);
	},

	setupController: function(controller, model) {
		this._super(controller, model);
		controller.set("taskId", this.get("taskId"));
	}
});
