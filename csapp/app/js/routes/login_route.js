App.LoginRoute = Ember.Route.extend({
	model: function() {
		return {
			username: "",
			password: ""
		};
	},

	actions: {
		transitionToLauncher: function() {
			this.transitionTo("clouds");
		},

		transitionToLauncherAt: function(routeName) {
			this.transitionTo(routeName);
		}
	},
});
