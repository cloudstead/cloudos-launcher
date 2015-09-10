LauncherStorage = {
	localStorage: window.localStorage,
	sessionStorage: window.sessionStorage,

	saveLogin: function(username, password, token) {
		this.localStorage.setItem("username", username);
		this.sessionStorage.setItem("password", password);
		this.sessionStorage.setItem("token", token);
	},

	hasLoggedInUser: function() {
		var hasUsername = !Ember.isNone(this.localStorage.getItem("username"));
		var hasPassword = !Ember.isNone(this.sessionStorage.getItem("password"));

		return hasUsername && hasPassword;
	},

	removeLogin: function(username, password) {
		this.localStorage.removeItem("username");
		this.sessionStorage.removeItem("password");
		this.sessionStorage.removeItem("token");
	},

	getToken: function() {
		return this.sessionStorage.getItem("token");
	},
};
