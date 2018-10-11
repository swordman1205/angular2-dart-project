# Styleguide

## Conventions

* Do not nest selectors more than three levels deep!

## Naming
We use a variant of BEM with “blocks”, which works particularly well when combined with Angular components.

For example in Angular UserComponent we have general info part `general-info`, so in **user_component.scss**,we will use

```css
/* user_component.scss */
.general-info { }
.general-info--featured { }
.general-info__title { }
.general-info__content { }

```

 - **.general-info** is the “block” and represents the higher-level component
 - **.general-info__title** is an “element” and represents a descendant of `.general-info` that helps compose the block as a whole.
 - **.general-info--featured** is a “modifier” and represents a different state or variation on the `.general-info` block.


## Site layout
### .page
A page is the top most element and controls a 100% width and height of
the browser viewport
 - always have a direct URL in **app_component.dart**
 - URL is always placed directly under the domain (i.e. **#/admin** is the URL to the admin page etc.)
 - A page cannot contain other pages as child elements
 - Pages control their own layout and may have router-outlet to
 display sub components

Variants


### .page-component
A page component is the components placed inside the pages router-outlet, thus
the represents the main functionality and variations of the page
 - Page components cannot contain other page-component
 - A page should not display more than one page component at a time
 - Page components always have URL placed directly under the site URL (i.e.
  **#/admin/users** is the URL to the the component under the admin page which displays all users)

## File structure
We use the file nameing from Mediums CSS guide

# External Guide
We follow https://gist.github.com/cuibonobo/16f555c0047ab80044cf

## Links

* https://github.com/airbnb/css#oocss-and-bem
* https://csswizardry.com/2013/01/mindbemding-getting-your-head-round-bem-syntax/
* https://www.smashingmagazine.com/2011/12/an-introduction-to-object-oriented-css-oocss/
* https://medium.com/@fat/mediums-css-is-actually-pretty-fucking-good-b8e2a6c78b06