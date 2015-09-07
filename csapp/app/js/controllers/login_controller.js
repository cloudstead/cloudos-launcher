App.LoginController = Ember.ObjectController.extend({
	actions: {
		odLogin: function() {
			console.log("action");
			var loginResponse = API.login(this.get("username"), this.get("password"));
			if (loginResponse.status === 200) {
				this.send("transitionToLauncher");
			} else {
				$.notify("Wrong password", { position: "bottom-right", autoHideDelay: 10000, className: 'error' });
			}
		}
	}
});
