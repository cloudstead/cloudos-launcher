App.BetterCheckboxComponent = Ember.Component.extend({
	attributeBindings: ['type', 'value', 'checked', 'disabled'],
	tagName: 'input',
	type: 'checkbox',
	checked: false,
	disabled: false,

	_updateElementValue: function() {
		this.set('checked', this.$().prop('checked'));
	}.on('didInsertElement'),

	change: function(event){
		var appName = $(event.target).attr("id");
		var buttonVisibility = event.target.checked;
		var selector = "a[data-link-id='link_{ATTR}']".replace("{ATTR}", $(event.target).attr("id"));

		if (buttonVisibility) {
			$(selector).show();
		} else {
			$(selector).hide();
		}

		// REMEMBER CHOICE TO RECREATE IT ON RENDER PAGE
		if (Ember.isNone(DATA["apps"])){ DATA["apps"] = {};}
		DATA["apps"][appName] = buttonVisibility;

		this._updateElementValue();
		this.sendAction('action', this.get('value'), this.get('checked'));
	},
});