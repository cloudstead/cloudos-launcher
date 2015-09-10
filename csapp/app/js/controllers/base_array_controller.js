App.BaseArrayController = Ember.ArrayController.extend({
	previousTransition: null,

		doTransitionToPreviuosRoute: function() {
			if (Ember.isNone(this.get("previousTransition"))) {
				this.send("doTransitionTo", "index");
			} else {
				var prevTransition = this.get("previousTransition");
				this.set("previousTransition", null);
				prevTransition.retry();
			}
		}
});
