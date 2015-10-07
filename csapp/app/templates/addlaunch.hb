<div class="row">
	<div id="sidebar" class="medium-3 columns">
		<ul class="vertical menu-bar wiz_panel">
			<h3 class="wiz_panel_header">{{t config.sidebar_caption}}</h3>
			{{#each linkItem in model.linkItems}}
				<li><a {{action "updateSelection" linkItem.name}} class="unopened menu-item {{unbound linkItem.name}}">{{linkItem.value}}</a></li>
			{{/each}}
		</ul>
	</div>
	<div id="main" class="medium-9 columns">
		{{outlet}}
	</div>
	<div class="clearfix"></div>
	<div class="row">
		<button {{action 'doLaunch'}} class="button-small">{{t config.save}}</button>
		<button {{action 'doDownload'}} class="button-small">{{t config.download}}</button>
		<button {{action 'doCancel'}} class="button-small">{{t config.cancel}}</button>
	</div>
</div>
