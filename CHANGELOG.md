## 1.20.1 Changelog

### 2.1.1
- Fabric Configs now exist
- Metabolism HUD shows behind chat
- Added Decorative Blocks environment effects

### 2.1.0
- Appleskin compat & tooltips added for fabric
- Fixed stews and water bottles (fabric)

### 2.0.0
- Port to fabric
- Metabolite Tooltips for fabric will be implemented later.

### 1.4.0
- Changed values for all food items
- Metabolism effect amplifier works differently
- Metabolization reduces heat only when effect level is more than 0
- Metabolites for other mods are not OP anymore
- AdvancedLocationCheck can omit the type variable

### 1.3.1 
- Fixed metabolization not generating warmth properly
- Changed display of warmth bar to flicker when regenerating

### 1.1.0
- Fixed base heat resistance being ignored.  
- When there is no resource left to drain, the other resource starts draining.  
- Hunger effect drains food & hydration.  
- Nerfed high effects and added condition for having no blocks above. Multiplied by 1.5 at night.  
- Water bottles give hydration & consume warmth
- Meats rebalanced
- Compat for [Pagan's Blessing](https://www.curseforge.com/minecraft/mc-mods/pagans-blessing) foods

### 1.3.0
#### Metabolization
- Metabolization will generate the resource being drained, if there's less of it than the other.
- Otherwise warmth will always be generated if not maxed(20)
#### Metabolites
- Metabolites can now specify a *modifier* object
- Modifiers can change stack size, ability to be eaten when full, and fast eating
- Stews and soup are stackable up to 16
- Cakes have metabolite data and can stack up to 64
- Cookies are always eatable
#### Other changes
- Nerfed some environment effects, reduced resource drain to 80%
- Campfires give cold resistance, soul campfires give heat resistance
- Soul sand & soil are considered cold blocks
- Some changes to food values (more changes coming next version)

### [1.2.0]
- AdvancedLocationCheck loot condition can now check for biome tags
- Added GameRule to disable temperature
- Added commands to get, set, add metabolism variables

### [1.1.3]
- Fixes crash on server join ([#2](https://github.com/lilypuree/Metabolism/issues/2))

### [1.1.2]
- Fixes crash on dedicated server ([#1](https://github.com/lilypuree/Metabolism/issues/1))

### [1.1.1]
- Metabolism does not tick & show in gui when not on survival mode
- Water bottle tooltips added, gives 2 hydration
- Deep dark is cold
- Sprinting & swimming gives metabolism progress
- Other minor bug fixes






