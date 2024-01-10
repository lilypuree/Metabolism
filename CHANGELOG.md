### [1.1.0] (Minecraft 1.20.1)
- Fixed base heat resistance being ignored.  
- When there is no resource left to drain, the other resource starts draining.  
- Hunger effect drains food & hydration.  
- Nerfed high effects and added condition for having no blocks above. Multiplied by 1.5 at night.  
- Water bottles give hydration & consume warmth
- Meats rebalanced
- Compat for [Pagan's Blessing](https://www.curseforge.com/minecraft/mc-mods/pagans-blessing) foods

### [1.1.1] (Minecraft 1.20.1)  

- Metabolism does not tick & show in gui when not on survival mode
- Water bottle tooltips added, gives 2 hydration
- Deep dark is cold
- Sprinting & swimming gives metabolism progress
- Other minor bug fixes

### [1.1.2] (Minecraft 1.20.1)

- Fixes crash on dedicated server ([#1](https://github.com/lilypuree/Metabolism/issues/1)) 

### [1.1.3] (Minecraft 1.20.1)

- Fixes crash on server join ([#2](https://github.com/lilypuree/Metabolism/issues/2))

### [1.2.0] (Minecraft 1.20.1)

- AdvancedLocationCheck loot condition can now check for biome tags
- Added GameRule to disable temperature
- Added commands to get, set, add metabolism variables
- 
### [1.3.0] (Minecraft 1.20.1)
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

### [1.3.1] (Minecraft 1.20.1)
- Fixed metabolization not generating warmth properly
- Changed display of warmth bar to flicker when regenerating