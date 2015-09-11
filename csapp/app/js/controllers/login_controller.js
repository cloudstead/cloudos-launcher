App.LoginController = Ember.ObjectController.extend({
	previousTransition: null,

	actions: {
		odLogin: function() {
			var loginResponse = API.login(this.get("username"), this.get("password"));
			if (loginResponse.isSuccess()) {
				LauncherStorage.saveLogin(this.get("username"), this.get("password"), loginResponse.data.token);
				this.send("transitionToLauncherAt", this._resolveNextRoute());
			} else {
				$.notify(loginResponse.data, { position: "bottom-right", autoHideDelay: 10000, className: 'error' });
			}
		}
	},

	_resolveNextRoute: function() {
		var route = "clouds";

		var clouds = App.CloudModel.getAll();

		if (Ember.isEmpty(clouds)) {
			route = "add_cloud";
		}

		return route;
	}
});
