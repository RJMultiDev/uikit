# uikit

Kotlin rewrite of the `com.hwkid.uikit` library decompiled from
`hwWatchSettings.apk`. Resources are vendored with the same names so
`androidx.appcompat:appcompat` + AGP can compile them into the AAR.

## Modules

- `uikit` — Android library, namespace `com.hwkid.uikit`.

## Public classes

- `HwKidButton`, `HwKidCheckBox`, `HwKidRadioButton`, `HwKidSwitch`,
  `HwKidTextView`
- `HwKidListView`, `HwKidRecyclerView`
- `HwKidToast` (object)
- `OverScrollDecoratorHelper` (object)
- `VerticalOverScrollBounceEffectDecorator`, `HorizontalOverScrollBounceEffectDecorator`

## Build locally

```bash
./gradlew :uikit:assembleRelease
```

## Build in CI

The GitHub Actions workflow at
`.github/workflows/android-build.yml` builds the library on every push and
uploads the resulting AAR as a workflow artifact.
