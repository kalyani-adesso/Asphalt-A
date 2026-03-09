# MVVM + Clean Architecture

This app follows **MVVM** for presentation and **Clean Architecture** for layering and dependency direction.

## Layers

```
┌─────────────────────────────────────────────────────────┐
│  Presentation (Views + ViewModels)                       │
│  - SwiftUI Views: bind to ViewModel @Published state    │
│  - ViewModels: depend only on Use Case protocols        │
└───────────────────────────┬─────────────────────────────┘
                            │ uses
┌───────────────────────────▼─────────────────────────────┐
│  Domain (Use Cases + Repository protocols)              │
│  - Use case protocols: one app action per use case      │
│  - Repository protocols: data access abstraction        │
└───────────────────────────┬─────────────────────────────┘
                            │ implemented by
┌───────────────────────────▼─────────────────────────────┐
│  Data (Use Case impls + Repository impls)               │
│  - Use case implementations call repository protocols   │
│  - Repository implementations wrap shared / API         │
└─────────────────────────────────────────────────────────┘
```

## Folder structure

- **`Domain/`**
  - **`Repositories/`** – Protocol definitions (e.g. `RideSummaryRepositoryProtocol`, `UserProfileRepositoryProtocol`).
  - **`UseCases/`** – Use case protocols (e.g. `GetRideSummaryUseCaseProtocol`, `GetUserProfileUseCaseProtocol`).

- **`Data/`**
  - **`Repositories/`** – Concrete repositories implementing Domain protocols (e.g. `RideSummaryRepository`, `UserProfileRepository`).
  - **`UseCases/`** – Use case implementations that depend on repository protocols (e.g. `GetRideSummaryUseCase`, `GetUserProfileUseCase`).

- **`Presentation/`** (feature folders: `HomeView/`, `UpcomingRide/`, etc.)
  - **Views** – SwiftUI; no business logic, only bindings and layout.
  - **ViewModels** – Depend on **use case protocols** only; no direct repository or API access.

- **`Core/DI/`**
  - **`AppDependencyContainer`** – Builds repositories and use cases, provides a default `HomeViewModel()` and can be extended for other screens.

## Dependency rule

- **Presentation** → Domain (use case protocols only).
- **Domain** → nothing (no imports from Data or Presentation).
- **Data** → Domain (implements protocols) and shared/API as needed.

ViewModels are testable by injecting mock use cases. Use cases are testable by injecting mock repositories.

## Example: Home flow

1. **View** (`HomeView`) uses `@StateObject var home = HomeViewModel()` (container provides default use cases via `init()`).
2. **ViewModel** (`HomeViewModel`) holds `GetRideSummaryUseCaseProtocol` and `GetUserProfileUseCaseProtocol`; calls `getRideSummaryUseCase.execute(...)` and `getUserProfileUseCase.execute()`.
3. **Use case** (e.g. `GetRideSummaryUseCase`) calls `RideSummaryRepositoryProtocol.getRideSummary(...)`.
4. **Repository** (e.g. `RideSummaryRepository`) uses shared `RidesRepository` / API and returns result to the use case → ViewModel → View.

## Adding a new feature

1. **Domain:** Add repository protocol(s) and use case protocol(s) in `Domain/`.
2. **Data:** Add repository implementation(s) and use case implementation(s) in `Data/`.
3. **DI:** Register and expose the new use case (and ViewModel if needed) in `AppDependencyContainer`.
4. **Presentation:** Add or update ViewModel to take the use case protocol in `init` and call it; keep Views dumb.
