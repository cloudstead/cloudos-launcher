App.LoginController = Ember.ObjectController.extend({
	previousTransition: null,

	actions: {
		odLogin: function() {
			var loginResponse = API.login(this.get("username"), this.get("password"));
			if (loginResponse.isSuccess()) {
				LauncherStorage.saveLogin(this.get("username"), this.get("password"), loginResponse.data.token);
				this.send("transitionToLauncher");
			} else {
				$.notify(loginResponse.data, { position: "bottom-right", autoHideDelay: 10000, className: 'error' });
			}
		}
	}
});
