App.ManageSshKeysController = App.BaseArrayController.extend({

	actions: {
		goToNewShhKey: function(fileContent) {
			this.send("doTransitionTo", "new_ssh_key");
		},
		doDelete: function(ssh_key) {
		},
	},
});
