App.LauncherController = App.BaseObjectController.extend({
	actions: {
		doLogout: function() {
			LauncherStorage.removeLogin();
			this.send("doTransitionToLogin");
		}
	}
});
