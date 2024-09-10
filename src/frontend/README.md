# Frontend documentation

Please download and use the vscode extensions "Prettier" for formatting code.
We are using tailwind CSS and fontawesome icons.

Icons used can be found [here](https://fontawesome.com/icons)
If you want to use for example `fa-user`, then this is how it would look like
```ts
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faUser } from '@fortawesome/free-solid-svg-icons';
@Component({
  import: [FontAwesomeModule],
  ...
})
export class yourClassComponent {
  faUser = faUser;
}
```
Then in HTML file:
```html
<fa-icon [icon]="faUser"></fa-icon>
```

For using the flags inline with text add the classes .fi and .fi-xx (where xx is the [ISO 3166-1-alpha-2](https://www.iso.org/obp/ui/#search) code of a country)

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

To run on firefox: `ng test --browsers Firefox`

For CI or if you don't want to open browser then run in Headless mode. You will need to have installed dev packages.
```bash
ng test --no-watch --no-progress --browsers=FirefoxHeadless
```

## Localisation usage
For localisation use the **@angular/localize** module.

##### Extract translate file
Use Angular CLI commands to retrieve messages from your application:
```
ng extract-i18n
```
This command generates a `messages.xlf` file in the `src/locale` directory. Translators use this file for translations.

##### Translation Markup
Use the *i18n* attributes in your HTML templates to mark that this text should have translate form:
```
<h1 i18n="@@test.multilanguage">Multi-language</h1>
```
[*Optional*] Provide a static indentificator for this attribute using `@@id`. It will help avoid repeating and better understanding the segment for a translator.

##### Plural usage
If you have an unknown number of elements, and you want to provide the correct word form describing it use **plurals**:
```
<p i18n="@@item" [ngPlural]="count">
  { count, plural, =0 {No items} =1 {One item} other {# items}}
</p>
```
This functionality is not so good for the English language, but useful in others. **Example result table**: 
| Count | English form | Lithuanian form |
| ----- | --------- | --------- |
| 0     | elements  | elementų
| 1     | element   | elementas
| few   | elements  | elementai
| many  | elements  | elementų
| other | elements  | elementai

##### Translation
In the `src/locale` directory several files. `messages.xlf` is the general one which is extracted. Other file names include language codes, for example for Lithuanian(LT) is `messages.lt.xlf`. To translate text in this file place *<target>* tag after *<source>* tag. Example:
```
<trans-unit id="test.multilanguage" datatype="html">
  <source>Multi-language</source>
  <target>Daugiakalbiškumas</target>
</trans-unit>
```

**Note**: You can use **Poedit** software for more comfortable translations. 

##### See translation result

Using the development server, it will not be possible to switch between language versions of the site.
Use `ng serve` to see only the English version of the product.
Use `ng serve --configuration=lt --open` to see only the Lithuanian version of the product.

Build the project `ng build` to have the possibility swicht between different language versions of the platform. 

##### Additional functionality
There are presented only the necessary steps for successful translation. Other features like tag descriptions, gender management, etc. can be found in official documentation: https://angular.io/guide/localizing-angular.
