App.LaunchCloudsteadView = Ember.View.extend({
	didInsertElement: function() {
		console.log("Insert Element");
		this.get("controller").startWatchingTaskProgress();
	}
});
