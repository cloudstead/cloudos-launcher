App.LoginController = Ember.ObjectController.extend({
	previousTransition: null,

	actions: {
		odLogin: function() {
			var loginResponse = API.login(this.get("username"), this.get("password"));
			if (loginResponse.status === 200) {
				LauncherStorage.saveLogin(this.get("username"), this.get("password"), loginResponse.data);
				this.send("transitionToLauncher");
			} else {
				$.notify("Wrong password", { position: "bottom-right", autoHideDelay: 10000, className: 'error' });
			}
		}
	}
});
