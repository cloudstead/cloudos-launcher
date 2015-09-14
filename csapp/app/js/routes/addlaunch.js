//"Addlaunch" ROUTES DEFINITIONS

App.AddlaunchRoute = App.ProtectedRoute.extend({
	wizard: Em.I18n.translations.wizard,
	actions: {
		goToSubRoute: function(data){
			if(data === "apps"){
				// this.transitionTo('app/');
				window.location.href = "#/launcher/addlaunch/apps/app/default";
			}else{
				this.transitionTo(data);
			}
		}
	},
	model: function(){
		var data = {linkItems:[]};
		ADD_LAUNCH_ROUTES.forEach(function(routeName){
			data.linkItems.push({
				name: routeName,
				value: trans[routeName] !== undefined ? trans[routeName]["tab"] : routeName.toUpperCase()
			});
		});
		return data;
	},
});

ADD_LAUNCH_ROUTES.forEach(function(route){
	var r = route.ToRoute();

	// Create a route for every tab.
	App[r] = Ember.Route.extend({
		content_model : {},
		model: function(params){
			return getModels(route, params);
		},
		renderTemplate: function() {
			if (route === 'apps') {
				this.render('apps');
			} else {
				this.render('generic');
			}
		},
		afterModel: function(model, transition) {
			transition.then(function() {
				// Done transitioning
				Ember.run.schedule('afterRender', self, function () {
					// IF TAB WAS ALREADY OPEN, VALIDATE FIELD TO SHOW ERROR IF ERROR EXIST
					var isUnopenedTab = $("a.selected").hasClass('unopened');
					if(!isUnopenedTab){
						Validator.ValidateActiveTab();
					}else{
						$("a.selected").removeClass('unopened');
					}

					// BIND EVENT TO VALIDATE ON FOCUS OUT
					$.each(Validator.ElementsForValidation(), function(index, elem){
						$(elem).bind("focusout", function(e) {
							var hasNoError = Validator.ValidateField(e.target);
							console.log('hasNoError? ', hasNoError);
							if(hasNoError){ $(e.target).removeClass("errorBorder"); }
						});
					});
				});
			});
		}
	});
});

// ADD_LAUNCH -> APPS TAB
App.ApplRoute = Ember.Route.extend({
	model: function(params){
		var data = {}, appName;
		data.apps = [];
		var classes = "button tiny apss_button", applyClass;
		if(params.app_name === "default"){
			data.isList = true;
			for (var app in APPS.app) {
				appName = APPS.app[app];
				applyClass = DATA["apps"] !== undefined && DATA["apps"][appName] !== undefined && DATA["apps"][appName] === true ? classes + " display_block" : classes;
				data.apps.push({
					id: appName,
					appName: appName,
					isChecked: DATA["apps"] !== undefined && DATA["apps"][appName] !== undefined ? DATA["apps"][appName] === true : false,
					appLink: "#/apps/app/" + appName,
					linkId: "link_" + appName,
					displayClass: applyClass
				});
			}
		}else{
			data.isList = false;
			data.route = params.app_name;
		}
		return data;
	},
});
