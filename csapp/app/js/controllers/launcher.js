App.LauncherController = Ember.ObjectController.extend({
	actions: {
		doLogout: function() {
			LauncherStorage.removeLogin();
			this.send("doTransitionToLogin");
		}
	}
});
