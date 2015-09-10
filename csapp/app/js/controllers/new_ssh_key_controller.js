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
			API.post_ssh_key(this.get("model"));
		}
	},
});
