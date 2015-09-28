App.AddCloudController = App.BaseObjectController.extend({

	isEdit: false,

	actions: {
		doUpdate: function () {
			console.log("UPDATING");
			var self = this;
			var cloud = this.get("model");
			cloud.update().then(function(response) {
				self.handleUpdateSuccess(response);
			}, function(reason){
				self.handleUpdateFailure(reason);
			});
		},

		doCancel: function() {
			this.doTransitionToPreviuosRoute();
		}
	},

	handleUpdateSuccess: function(response) {
		console.log("FAIL: ", response);
		console.log("SUCCESS: ");
		this.doTransitionToPreviuosRoute();
	},

	handleUpdateFailure: function(reason) {
		if (reason.status === 200) {
			console.log("SUCCESS2");
			this.handleUpdateSuccess(reason);
		} else if (reason.status === 403) {
			this.send("handleForbiddenResponse");
		}
	},
});
