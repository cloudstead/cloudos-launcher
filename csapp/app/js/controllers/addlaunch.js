var ADD_LAUNCH_ROUTES = [];
var additional_routes = ['apps'];
var trans =  getFirstTranslation();

for(var route in INIT_CONFIG) {
	ADD_LAUNCH_ROUTES.push(route);
}

additional_routes.forEach(function(route){
	ADD_LAUNCH_ROUTES.push(route);
});

App.ApplController = Ember.ObjectController.extend({
	fields: function() {
		var currentRoute = this.get("model.route");
		return App.FieldModel.createAppFields(currentRoute);
	}.property("model"),

	fileFields: function() {
		var currentRoute = this.get("model.route");
		return App.FieldModel.createFileAppFields(currentRoute);
	}.property("model"),

	fieldDataObserver: function() {
		var self = this, model = self.get('model'), dataObj, key = "appls";
		if(model.route === undefined) { return ;}
		if (Ember.isNone(DATA[key])) { DATA[key] = {}; }
		if(Ember.isNone(DATA[key][model.route])){ DATA[key][model.route] = {}; }

		DATA[key][model.route]["fields"] = self.get("fields");
		DATA[key][model.route]["tabGroups"] = {};
	}.observes("fields.@each.value"),

	fileFieldDataObserver: function() {
		var self = this, model = self.get('model'), key = "appls";
		if(model.route === undefined) { return ;}
		if (Ember.isNone(DATA[key])) { DATA[key] = {}; }
		if(Ember.isNone(DATA[key][model.route])){ DATA[key][model.route] = {}; }

		DATA[key][model.route]["files"] = self.get("fileFields");
		DATA[key][model.route]["tabGroups"] = {};
	}.observes("fileFields.@each.value"),
});

ADD_LAUNCH_ROUTES.forEach(function(route){
	var c = route.toController();

	// Create a controller for every route.
	App[c] = Ember.ObjectController.extend({
		title: Ember.String.htmlSafe(trans[route]['title']),
		description: Ember.String.htmlSafe(trans[route]['description']),
		routeName: route,

		fields: function() {
			return App.FieldModel.createFields(this.get("model"), trans[route]);
		}.property(),

		fileFields: function() {
			return App.FieldModel.createFileFields(this.get("model"), trans[route]);
		}.property(),

		subTabGroups: function() {
			var subTabString = this.get("model.sub_tabs");

			console.log("Sub tabs: ", subTabString);

			if (Ember.isEmpty(subTabString)) {
				return [];
			}

			var subTabs = App.SubTabModel.createArrayUsingDataFrom(this.get("model.sub_tabs"), this.get("routeName"));

			// var subTabs = App.SubTabModel.createArrayUsingListOfNames(
			// 	subTabList, this.get("routeName"), this.get("model"), trans[route]);

			return this._setSubTabGroups(subTabs);
		}.property(),

		isLaunch: function() {
			return this.routeName === 'launch';
		}.property(),

		fieldDataObserver: function() {
			var dataObj = {};
			var self = this;

			dataObj["field_prefix"] = this.get("model.field_prefix");
			dataObj["fields"] = self.get("fields");
			dataObj["tabGroups"] = {};
			DATA[self.get("routeName")] = dataObj;
		}.observes("fields.@each.value"),

		fileFieldDataObserver: function() {
			var dataObj = {};
			var self = this;

			dataObj["files"] = self.get("fileFields");
			dataObj["tabGroups"] = {};
			dataObj["field_prefix"] = this.get("model.field_prefix");

			DATA[self.get("routeName")] = dataObj;

			DATA[self.get("routeName")]["files"].forEach(function(field, index){
				readURL($("[data-element-id="+field.elementId+"]")[0], DATA[self.get("routeName")]['files'], index);
			});
		}.observes("fileFields.@each.value"),

		appCheckObserver: function() {
		}.observes("apps.@each.isChecked"),

		actions: {
			activateTab: function(tab) {
				// OFF EVENTS ON CURRENTLY ACTIVE TAB
				$.each(Validator.ElementsOnActiveTab(), function(index, elem){
					$(elem).unbind("focusout", "**");
				});

				console.log("ACTIVATE TAB!!!");
				var tabGroup = this.get("subTabGroups").find(function(group){
					return group.tabGroupName === tab.get("tabGroup");
				});
				tabGroup.tabs.forEach(function(subTab) {
					subTab.set("isActive", false);
				});
				tab.set("isActive", true);
			},

			doLaunch: function() {
				var unopened = $('.unopened');
				unopened.removeClass('unopened');
				unopened.addClass('error_link');
				var errors = $("#sidebar>ul>li>a.menu-item.error_link");
				if(errors.length === 0 && unopened.length === 0){
					ZipGeneratorService.generateZipFrom(DATA);
				}else{
					$("button.launch_button").notify( "Please correct the errors", Validator.NotifyOptions );
				}
			},
			goToApp: function(data){
				console.log("goToApp: ", data);
			}
		},

		_setSubTabGroups: function(subTabs) {
			var self = this;
			// var model = self.get("model");
			var tabGroupNames = [GENERAL_SUBTAB_GROUP];
			var tabGroups = [];

			subTabs.forEach(function(subTab){
				var tabGroup = null;

				if (tabGroupNames.indexOf(subTab.get("tabGroup")) === -1) {
					// Create a new tab group.
					tabGroupNames.push(subTab.get("tabGroup"));

					// This is the first tab in this group, so activate it.
					subTab.set("isActive", true);

					// var translation = trans[route]['sub_tabs'][subTab.get("tabGroup")];

					tabGroup = App.TabGroupModel.createNew(subTab.get("tabGroup"), self.get("model.sub_tabs"));

					tabGroups.push(tabGroup);

				} else {
					tabGroup = tabGroups.find(function(group){
						return group.tabGroupName === subTab.get("tabGroup");
					});
				}

				tabGroup.addTab(subTab);
			});

			return tabGroups;
		}

	});
});

App.AddlaunchController = App.BaseObjectController.extend({
	actions: {
		updateSelection: function(data){
			Validator.ValidateActiveTab();

			$("#sidebar>ul>li>a.menu-item").removeClass("selected");
			$("#sidebar>ul>li>a.menu-item." + data).addClass("selected");
			this.send('goToSubRoute', data);
		},
		activateApp: function(data){
			console.log("activateApp", data);
		},
		doCancel: function() {
			this.doTransitionToPreviuosRoute();
		},
		doLaunch: function() {
			var unopened = $('.unopened');
			unopened.removeClass('unopened');
			unopened.addClass('error_link');
			var errors = $("#sidebar>ul>li>a.menu-item.error_link");
			var dataBlob = "";
			if(errors.length === 0 && unopened.length === 0){
				dataBlob = ZipGeneratorService.generateContentFrom(DATA, "base64");

				console.log("DATA: ", DATA);
				console.log("BLOB: ", dataBlob);

				var config = App.ConfigModel.create({
					name: DATA.dns.fields[0].value + " - " + DATA.dns.fields[1].value,
					base64zipData: dataBlob
				});

				config.update();
			}else{
				console.log("");
				$.notify("Please correct the errors", Validator.NotifyOptions );
			}
		},
		doDownload: function() {
			var unopened = $('.unopened');
			unopened.removeClass('unopened');
			unopened.addClass('error_link');
			var errors = $("#sidebar>ul>li>a.menu-item.error_link");
			var dataBlob = "";
			console.log("DATA: ", DATA);
			// if(errors.length === 0 && unopened.length === 0){
				dataBlob = ZipGeneratorService.generateZipFrom(DATA);
			// }else{
			// 	$notify( "Please correct the errors", Validator.NotifyOptions );
			// }
		}
	}
});

Ember.View.reopen({
	init: function() {
		this._super();
		var self = this;
		var attr = self.get('attributeBindings');
		// bind attributes beginning with 'data-'
		Em.keys(this).forEach(function(key) {
			if (key.substr(0, 5) === 'data-') {
				attr.pushObject(key);
			}
		});
	},
});
