App.NewSshKeyController = App.BaseObjectController.extend({
	previousTransition: null,

	actions: {
		doReadUploadedFile: function(fileContent) {
			this.set("publicKey", fileContent);
		},
		doCancel: function() {
			this.doTransitionToPreviuosRoute();
		},
		doAddKey: function() {
			var sshKey = this.get("model");
			if (sshKey.update()) {
				this.doTransitionToPreviuosRoute();
			}
		}
	},
});
