App.NewSshKeyController = App.BaseObjectController.extend({
	previousTransition: null,

	actions: {
		doReadUploadedFile: function(fileContent) {
			this.set("publicKey", fileContent);
		},

		doCancel: function() {
			this.doTransitionToPreviuosRoute();
		},

		doAddKey: function () {
			var self = this;
			var sshKey = this.get("model");
			sshKey.update().then(function(response) {
				self.handleUpdateSuccess(response);
			}, function(reason){
				self.handleUpdateFailure(reason);
			});
		},
	},

	handleUpdateSuccess: function(response) {
		this.doTransitionToPreviuosRoute();
	},

	handleUpdateFailure: function(reason) {
		if (reason.status === 200) {
			this.handleUpdateSuccess(reason);
		} else if (reason.status === 403) {
			this.send("handleForbiddenResponse");
		}
	},
});
