



# modules

## future modules

`tvmJvmAstUtilProject` (`<root>/jvmAstUtil`)
  - on `tvmJvmAstProject` (`<root>/jvmAstLib`)

`tvmCompilerInternalJvmAstBaselinesProject` (`<root>/compilerInternals/JvmAstBaselines`)
  - "pure"
  - also uses `tvmJvmAstProject` (`<root>/jvmAstLib`)

`tvmCompilerInternalBufferedJvmAstProject` (`<root>/compilerInternals/BufferedJvmAst`)
  - on `tvmJvmAstProject` (`<root>/jvmAstLib`)
  - also uses `tvmJvmAstUtilProject` (`<root>/jvmAstUtil`)
  - also uses `tvmCompilerIrAstProject` (`<root>/compilerIrAst`)
  - also uses `tvmCompilerInternalClassFileResolverAndParserProject` (`<root>/compilerInternals/ClassFileResolverAndParser`)

`tvmCompilerInternalJvmAstTransformsAndOptimsLibProject` (`<root>/compilerInternals/JvmAstTransformsAndOptimsLib`)
  - on `tvmJvmAstProject` (`<root>/jvmAstLib`)
  - uses `tvmCompilerInternalDependencyGraphProcessingProject` (`<root>/compilerInternals/DependencyGraphProcessing`)
  - uses `tvmCompilerInternalJvmAstBaselinesProject` (`<root>/compilerInternals/JvmAstBaselines`)
  - uses `tvmJvmAstUtilProject` (`<root>/jvmAstUtil`)

`tvmCompilerInternalDebugTreeProject` (`<root>/compilerInternals/DebugTree`)
  - "pure"
  - also uses `tvmJvmAstProject` (`<root>/jvmAstLib`)

`tvmCompilerInternalClassFileResolverAndParserProject` (`<root>/compilerInternals/ClassFileResolverAndParser`)
  - on `tvmCompilerUtilProject` (`<root>/compilerUtil`)
  - also uses `tvmJvmAstProject` (`<root>/jvmAstLib`)
  - also uses `tvmJvmAstUtilProject` (`<root>/jvmAstUtil`)

`tvmCompilerInternalDependencyGraphProcessingProject` (`<root>/compilerInternals/DependencyGraphProcessing`)
  - on `tvmCompilerUtilProject` (`<root>/compilerUtil`)
  - also uses `tvmCompilerInternalBufferedJvmAstProject` (`<root>/compilerInternals/BufferedJvmAst`)
  - also uses `tvmJvmAstProject` (`<root>/jvmAstLib`)
  - also uses `tvmCompilerInternalJvmAstTransformsAndOptimsLibProject` (`<root>/compilerInternals/JvmAstTransformsAndOptimsLib`)
  - also uses `tvmJvmAstUtilProject` (`<root>/jvmAstUtil`)
  - also uses `tvmCompilerInternalClassFileResolverAndParserProject` (`<root>/compilerInternals/ClassFileResolverAndParser`)
  - also uses `tvmCompilerInternalJvmAstBaselinesProject` (`<root>/compilerInternals/JvmAstBaselines`)

`tvmCompilerIrAstOptimizerProject` (`<root>/compilerIrAstOptimizer`)
  - on `tvmCompilerIrAstProject` (`<root>/compilerIrAst`)
  - also uses `tvmJvmAstProject` (`<root>/jvmAstLib`)
  - also uses `tvmJvmAstUtilProject` (`<root>/jvmAstUtil`)


