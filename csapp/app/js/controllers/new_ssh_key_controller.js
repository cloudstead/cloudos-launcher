App.NewSshKeyController = Ember.ObjectController.extend({
	previousTransition: null,

	actions: {
		doReadUploadedFile: function(fileContent) {
			this.set("publicKey", fileContent);
		},
		doCancel: function() {
			if (Ember.isNone(this.get("previousTransition"))) {
				this.send("doTransitionToPreviuosRoute");
			} else {
				var prevTransition = this.get("previousTransition");
				this.set("previousTransition", null);
				prevTransition.retry();
			}
		},
		doAddKey: function() {
			API.post_ssh_key(this.get("model"));
		}
	},
});
