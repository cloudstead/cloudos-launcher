
<div class="row margin_top">
	<div class="small-4 columns"><h3>{{t clouds.title}}</h3></div>
	<div class="small-4 columns">
		<button class="button-small" {{ action "doAddCloud" }}>{{t clouds.new_cloud}}</button>
	</div>
</div>
<div class="row">
	<table id="providersTable" width="100%">
		<thead>
			<tr style="text-align: center">
				<th class="text_align_center">{{t clouds.cloud_name}}</th>
				<th class="text_align_center">{{t clouds.cloud_provider}}</th>
				<th class="text_align_center">{{t clouds.cloud_actions}}</th>
			</tr>
		</thead>
		<tbody>
			{{#each cloud in arrangedContent }}
				<tr id="{{cloud.uuid}}">
					<td class="text_align_center">{{#link-to "edit_cloud" cloud.name }}{{cloud.name}}{{/link-to}}</td>
					<td class="text_align_center">{{cloud.vendor}}</td>
					<td class="text_align_center">
						<a class="button tiny" {{action "doDelete" cloud}}>{{t clouds.cloud_delete}}</a>
					</td>
				</tr>
			{{/each}}
		</tbody>
	</table>
</div>

<div id="confirmProviderDelete" class="reveal-modal" data-reveal aria-labelledby="modalTitle" aria-hidden="true" role="dialog">
	<h2 id="modalTitle">Remove cloud</h2>
	<p class="lead">Confirm deletion of Cloud: <span id="providerName" provider-id=""></span></p>
	<a class="button" {{action "removeProvider" this}}>Yes, Delete</a>
	<a class="close button" onclick="$('#confirmProviderDelete').foundation('reveal', 'close');">No, Cancel</a>
</div>
