



# modules

## future modules (journal 2026/04/27 - ... )

### Set 1

✅ `tvmJvmAstUtilProject` (`<root>/jvmAstUtil`)
  - on `tvmJvmAstProject`

✅ `tvmCompilerInternalJvmAstBaselinesProject` (`<root>/compilerInternals/JvmAstBaselines`)
  - "pure"
  - also uses `tvmJvmAstProject`

✅ `tvmCompilerInternalBufferedJvmAstProject` (`<root>/compilerInternals/BufferedJvmAst`)
  - on `tvmJvmAstProject`
  - also uses `tvmJvmAstUtilProject`
  - also uses `tvmCompilerIrAstProject`
  - also uses `tvmCompilerInternalClassFileResolverAndParserProject`

`tvmCompilerInternalAstDepsAndTransformsAndOptimsKitProject` (`<root>/compilerInternals/AstDepsAndTransformsAndOptimsLib`)
  - on `tvmCompilerUtilProject`
  - also uses `tvmJvmAstProject`
  - also uses `tvmCompilerInternalJvmAstBaselinesProject`
  - also uses `tvmCompilerInternalBufferedJvmAstProject`
  - also uses `tvmCompilerInternalClassFileResolverAndParserProject`
  - also uses `tvmJvmAstUtilProject`

✅ `tvmCompilerInternalDebugTreeProject` (`<root>/compilerInternals/DebugTree`)
  - "pure"
  - also uses `tvmJvmAstProject` 

✅ `tvmCompilerInternalClassFileResolverAndParserProject` (`<root>/compilerInternals/ClassFileResolverAndParser`)
  - on `tvmCompilerUtilProject`
  - also uses `tvmJvmAstProject`
  - also uses `tvmJvmAstUtilProject`

✅ `tvmCompilerIrAstOptimizerProject` (`<root>/compilerIrAstOptimizer`)
  - on `tvmCompilerIrAstProject`
  - also uses `tvmJvmAstProject`
  - also uses `tvmJvmAstUtilProject`

### Set 2


