// When cloud is selected it changes the cloud type which in turn changes regions and instances.
App.LaunchCloudsteadController = App.BaseObjectController.extend({

	percent_value: "",

	taskId: "",

	lastMessage: "",

	startWatchingTaskProgress: function() {
		var self = this;
		var taskId = this.get("taskId");
		var progressCheck = API.get_task(taskId);

		API.get_task(taskId).then(function(response){
			var lastEvent = response.events[response.events.length-1];
			var lastStatus = lastEvent.messageKey.split(".");

			if (lastStatus.length > 1 && lastStatus[0] === "setup") {
				var percent = 0;
				switch (lastStatus[1]) {
					case 'cheffing':
						percent = self._calculateCheffingPercent(lastStatus);
						break;
					case 'instanceLookup':
						percent = 70;
						break;
					case 'startingMasterInstance':
						percent = 85;
						break;
					case 'success':
						percent = 100;
						break;
					default:
						break;
				}

				var message = getObjectPropertyFromPath(
					getFirstTranslation()["launch_cloudstead"],
					lastEvent.messageKey
				);

				if (!Ember.isEmpty(message)) {
					self.set("lastMessage", message);
				}

				console.log("CLOUDSTEAD LAUNCH IN PROGRESS!: ", percent, lastEvent.messageKey, self.get("lastMessage"));

				self.set("percent_value", percent);
			}

			if (response.success === false) {
				setTimeout(function(){
					self.startWatchingTaskProgress();
				}, 1000);
			}
		});
	},

	_calculateCheffingPercent: function (status) {
		var percent = 10;
		if (status.length === 3) {
			var cheffing_percent_range = 50;
			var messageArtifacts = status[2].match(/percent_done_(\d+)/);
			var cheffing_percent = messageArtifacts != null ? parseInt(messageArtifacts[1]) : 0;
			percent += Math.round(cheffing_percent_range * cheffing_percent / 100);
		}
		return percent;
	},
});
