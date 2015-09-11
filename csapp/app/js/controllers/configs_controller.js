App.ConfigsController = App.BaseArrayController.extend({
	actions: {
		removeConfig: function () {
		},

		doNewConfig: function() {
			this.send("doTransitionTo", "addlaunch");
		}
	}
});
