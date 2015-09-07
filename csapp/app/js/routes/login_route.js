App.LoginRoute = Ember.Route.extend({
	model: function() {
		return {
			username: "",
			password: ""
		};
	},

	actions: {
		transitionToLauncher: function() {
			this.transitionTo("launcher");
		}
	},
});
