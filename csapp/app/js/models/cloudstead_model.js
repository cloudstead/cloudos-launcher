App.CloudsteadModel = Ember.Object.extend({
});

App.CloudsteadModel.reopenClass({
	createNewEmpty: function() {
		return App.CloudsteadModel.create({
			name: "",
			privateKey: "",
			user: "",
			region: "",
			cloud: "",
			instanceType: "",
			instanceId: "",
			sshKey: "",
			launchConfig: "",
			additionalApps: "",
			keyPassphrase: "",
		});
	}
});
