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
		var configs = App.ConfigModel.getAll();
		var cloudsteads = App.CloudsteadModel.getAll();

		if (Ember.isEmpty(clouds)) {
			route = "add_cloud";
		} else if (Ember.isEmpty(configs)) {
			route = "addlaunch";
		} else if (Ember.isEmpty(cloudsteads)) {
			route = "add_cloudstead";
		}

		return route;
	}
});
