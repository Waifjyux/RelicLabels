# Relic Labels API

## Maven Dependency

Add the following dependency to your `pom.xml` to use Relic Labels in your project:

```xml
<dependency>
  <groupId>io.github.waifjyux</groupId>
  <artifactId>reliclabels</artifactId>
  <version>1.0.3</version>
</dependency>
```
⠀
## Adding New Tiers
You can create and add a new tier with the following code:
```java
// Create a tier using a name and a color
RelicTier tier = new RelicTier("rare", "#3493c9");
// Add the tier to the list of tiers
RelicTier.addTier(tier);
// Save the config
RelicTier.saveConfig();
```
⠀
## Adding a Relic Label to an Item
To apply a relic label to an item:
```java
// Get the tier you defined
RelicTier tier = RelicTier.getTier("rare");

// Create a Relic Label for your item
RelicLabel label = new RelicLabel(5, "Bloodsong", "rare dagger", tier);
label.addAutoLineContent("§7Deals §c5% §7more damage to enemies with less than §c50% §7health.");
label.addStrikethrough();
label.addAutoLineContent("§7A relic of the past, once wielded by a legendary assassin.");

// Add the Relic Label to the metadata of the item
ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
label.generate(item);

player.getInventory().addItem(item);
```
⠀
## Compiling the Resource Pack
To compile the resource pack:
```java
// Compile the resource pack to the plugin folder as .zip
// Throws IOException
ResourcePackCompiler.compile();

// Add all resources to ItemsAdder
// Throws IOException
ItemsAdderIntegration.compile();
```
