App.ManageSshKeysController = Ember.ArrayController.extend({

	actions: {
		goToNewShhKey: function(fileContent) {
			this.send("doTransitionToNewSshKey");
		},
		doDelete: function(ssh_key) {
		},
	},
});
