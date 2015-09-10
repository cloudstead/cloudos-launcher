
<div class="row margin_top">
	<div class="small-4 columns"><h3>Manage SSH Keys</h3></div>
	<div class="small-4 columns">
		<button class="button-small" {{action "goToNewShhKey"}}>New SSH Key</button>
	</div>
</div>
<div class="row">
	<table id="providersTable" width="100%">
		<thead>
			<tr style="text-align: center">
				<th class="text_align_center">Name</th>
				<th class="text_align_center">Delete</th>
			</tr>
		</thead>
		<tbody>
			{{#each sshKey in arrangedContent }}
				<tr id="{{sshKey.uuid}}">
					<td class="text_align_center">{{sshKey.name}}</td>
					<td class="text_align_center"><a class="button tiny" {{action "doRemoveKey" sshKey.uuid sshKey.name}}>Delete</a></td>
				</tr>
			{{/each}}
		</tbody>
	</table>
</div>
<div class="row">
	{{#link-to 'add_cloudstead' classNames="button"}}Done{{/link-to}}
</div>
