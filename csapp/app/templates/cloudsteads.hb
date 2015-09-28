
<div class="row margin_top">
	<div class="small-4 columns"><h3>{{t cloudsteads.title_update}}</h3></div>
	<div class="small-4 columns">
		<button class="button-small" {{ action "doAddCloudstead" }}>
			{{t add_cloudstead.new_cloudstead}}
		</button>
	</div>
</div>
<div class="row">
	<table id="providersTable" width="100%">
		<thead>
			<tr style="text-align: center">
				<th class="text_align_center">{{t cloudsteads.cloudstead_name}}</th>
				<th class="text_align_center">{{t cloudsteads.cloud}}</th>
				<th class="text_align_center">{{t cloudsteads.cloudstead_config}}</th>
				<th class="text_align_center">{{t cloudsteads.status}}</th>
				<th class="text_align_center">{{t cloudsteads.actions}}</th>
			</tr>
		</thead>
		<tbody>
			{{#each cloudstead in arrangedContent }}
				<tr id="{{cloudstead.id}}">
					<td class="text_align_center">{{#link-to "edit_cloudstead" cloudstead.name }}{{cloudstead.name}}{{/link-to}}</td>
					<td class="text_align_center">{{cloudstead.cloud}}</td>
					<td class="text_align_center">{{cloudstead.launch_config}}</td>
					<td class="text_align_center">{{cloudstead.status}}</td>
					<td class="text_align_center">
						<a class="button tiny" {{action "confirmProviderRemove" cloudstead.id cloudstead.name}}>
							{{t cloudsteads.cloudstead_delete}}
						</a>
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
