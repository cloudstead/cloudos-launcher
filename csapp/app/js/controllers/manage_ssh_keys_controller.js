App.ManageSshKeysController = App.BaseArrayController.extend({

	actions: {
		goToNewShhKey: function(fileContent) {
			this.send("doTransitionTo", "new_ssh_key");
		},

		doDelete: function(ssh_key) {
			var self = this;
			ssh_key.destroy().then(function(response) {
				self.handleDeleteSuccess(response, ssh_key);
			}, function(reason){
				self.handleDeleteFailure(reason, ssh_key);
			});
		},
	},

	handleDeleteSuccess: function(response, ssh_key) {
		this.get('content').removeObject(ssh_key);
	},

	handleDeleteFailure: function(reason, ssh_key) {
		if (reason.status === 200) {
			this.handleDeleteSuccess(reason, ssh_key);
		} else if (reason.status === 403) {
			self.send("handleForbiddenResponse");
		}
	},

});
