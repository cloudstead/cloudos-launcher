App.AddCloudsteadRoute = App.BaseCloudsteadRoute.extend({
	model: function() {
		return App.CloudsteadModel.createNewEmpty();
	},

	setupController: function(controller, model) {
		this._super(controller, model);

		this.loadRelatedData(controller, model);

		controller.set("isEdit", false);
	},
});
