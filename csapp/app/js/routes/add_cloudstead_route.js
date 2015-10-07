App.AddCloudsteadRoute = App.ProtectedRoute.extend({
	model: function() {
		return App.CloudsteadModel.createNewForAdd();
	},

	setupController: function(controller, model) {
		this._super(controller, model);

		// this.loadRelatedData(controller, model);

		controller.set("isEdit", false);
	},
});
