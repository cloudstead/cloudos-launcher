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

	addTask: function (taskId) {
		// Create 'activeTask' key in localStorage if it doesn't exist.
		if (Ember.isNone(this.localStorage.getItem("activeTasks"))) {
			this.localStorage.setItem("activeTasks", []);
		}
		// add item to active tasks if it isn't already there.
		if (!this.hasActiveTask(taskId)) {
			this.getAllActiveTasks().pushObject(taskId);
		}
	},

	removeTask: function (taskId) {
		if (this.hasActiveTask(taskId)) {
			this.getAllActiveTasks().removeObject(taskId);
		}
	},

	hasActiveTasks: function() {
		console.log("hasActiveTasks: ", this.getAllActiveTasks());
		return Ember.isEmpty(this.getAllActiveTasks());
	},

	hasActiveTask: function(taskId) {
		return this.getActiveTask(taskId) != null;
	},

	getActiveTask: function(taskId) {
		return this.getAllActiveTasks().find(function(task) {
			return task === taskId;
		});
	},

	getAllActiveTasks: function() {
		return this.localStorage.getItem("activeTasks").split(",");
	},
};
