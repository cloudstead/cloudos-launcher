{{#if isList }}
	{{#each app in apps}}
		<div class='row wiz_dns_tab'>
			<div class='large-1 columns switch tiny radius'>
				{{better-checkbox id=app.appName name=app.appName checked=app.isChecked  value=model.value}}
				<label {{bind-attr for=app.appName}} class='apps_label'></label>
			</div>
			<div class='large-9 columns apps_label'>{{app.appName}}</div>
			{{#link-to 'appl' app.appName class=app.displayClass style=app.style data-link-id=app.linkId}}Configure{{/link-to}}
		</div>
	{{/each}}
{{else}}
	<p style="margin-bottom:0px; margin-left:20px;">{{#link-to 'appl' 'default' classNames="button small"}}Back{{/link-to}}</p>
	{{#each field in fields }}
		<div class='large-5 columns wiz_dns_tab'>
			{{#if field.isChoice}}
				<label class="hint--bottom" {{bind-attr data-hint=field.hint}}>
				{{field.label}}
				{{view Ember.Select
						content=field.choices
						optionValuePath="content.value"
						optionLabelPath="content.label"
						valueBinding=field.value
						data-element-id=field.elementId }}
				</label>
			{{else}}
				{{#if field.isTimeOfDay}}
					<label class="hint--bottom" {{bind-attr data-hint=field.hint}}>
						{{field.label}}
						{{ input
							type=field.type.typeName
							value=field.value
							title=field.info
							class="large-5 columns wiz_dns_tab time_of_day"
							data-element-id=field.elementId
						}}
					</label>
					<script>
						$('.time_of_day').timepicker({ 'scrollDefault': 'now' });
					</script>
				{{else}}
					{{#if field.isCheckBox}}
						<label class="hint--bottom" {{bind-attr data-hint=field.hint}} style="height: 50px; margin-top: 35px;">
							
							<div class="large-1 columns switch tiny radius" style="margin-right: 30px;">
								{{ input
									type=field.type.typeName
									value=field.value
									title=field.info
									class="ember-view"
									data-element-id=field.elementId
									data-kind=field.type.typeKind
									data-required=field.required
									id=field.elementId
								}}
								<label {{bind-attr for=app.appName}} data-bindattr-25="25" class="apps_label"></label>
							</div>
							{{field.label}}
						</label>
						
					{{else}}
						<label class="hint--bottom" {{bind-attr data-hint=field.hint}}>
							{{field.label}}
							{{ input
								type=field.type.typeName
								value=field.value
								title=field.info
								class="large-5 columns wiz_dns_tab"
								data-element-id=field.elementId
								data-kind=field.type.typeKind
								data-required=field.required
							}}
						</label>
					{{/if}}
				{{/if}}
			{{/if}}
		</div>
	{{/each}}
	{{#each fileField in fileFields }}
		<div class='large-5 columns wiz_dns_tab'>
			<label class="hint--bottom" {{bind-attr data-hint=field.hint}}>
				{{fileField.label}}
				{{ input
					type=fileField.type.typeName
					value=fileField.value
					title=fileField.help
					class="large-5 columns wiz_dns_tab"
					data-element-id=fileField.elementId
				}}
			</label>
		</div>
	{{/each}}

{{/if}}