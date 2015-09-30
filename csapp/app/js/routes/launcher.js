App.LauncherRoute = App.ProtectedRoute.extend({
	actions: {
		doTransitionToLogin: function() {
			this.transitionTo("login");
		},

		handleLaunchStart: function(launch_response){
			console.log("start tracking task: ", launch_response.uuid);
			$('.footer').removeClass("hide");
			$('#launch_progress').val(0);
			LauncherStorage.addTask(launch_response.uuid);
			this.startWatchingTaskProgress(launch_response.uuid);
		}
	},

	startWatchingTaskProgress: function(taskId) {
		var self = this;
		var progressCheck = API.get_task(taskId);

		$('#launch_progress').removeClass('hide');

		progressCheck.then(function(response){
			if (response.success === true) {
				console.log("CLOUDSTEAD LAUNCHED SUCCESSFULLY!");
				$('#launch_progress').val(100);

				// setTimeout(function(){
				// 	$('.footer').addClass("hide");
				// }, 1000);

			} else {
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

					console.log("CLOUDSTEAD LAUNCH IN PROGRESS!: ", percent);

					$('#launch_progress').val(percent);
				}

				setTimeout(function(){
					self.startWatchingTaskProgress(taskId);
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
	}
});
