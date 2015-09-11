App.BaseObjectController = Ember.ObjectController.extend({
	previousTransition: null,

		doTransitionToPreviuosRoute: function() {
			if (Ember.isNone(this.get("previousTransition"))) {
				this.send("doTransitionTo", "clouds");
			} else {
				var prevTransition = this.get("previousTransition");
				this.set("previousTransition", null);
				prevTransition.retry();
			}
		},
});
