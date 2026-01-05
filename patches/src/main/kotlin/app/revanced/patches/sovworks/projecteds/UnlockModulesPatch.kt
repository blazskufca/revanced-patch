package app.revanced.patches.sovworks.projecteds

import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.fingerprint
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import com.android.tools.smali.dexlib2.AccessFlags

@Suppress("unused")
val unlockModulesPatch = bytecodePatch(
    name = "Unlock Modules",
    description = "Forces ModuleManager to report all modules as Available."
) {
    compatibleWith("com.sovworks.projecteds")

    execute {
        // Fingerprint 1: Find the Enum Class (ModuleVersionStatus)
        val statusEnumFingerprint = fingerprint {
            // "Available", "NotPurchased", "NotExists"
            strings("Available", "NotPurchased")
            accessFlags(AccessFlags.PUBLIC, AccessFlags.ENUM)
        }

        // Fingerprint 2: Find the Implementation Class (ModuleManagerImpl)
        val managerFingerprint = fingerprint {
            // Unique string in constructor: "moduleVersionRepository size "
            strings("moduleVersionRepository size ")
            accessFlags(AccessFlags.PUBLIC, AccessFlags.FINAL)
        }

        val statusEnumClass = statusEnumFingerprint.classDef
        val managerClass = managerFingerprint.classDef

        // A. Get the Enum Field "Available"
        // We look for the first static field in the Enum that has the Enum's type.
        // Assuming "Available" is the first defined constant or using the logic from original snippet.
        val availableField = statusEnumClass.fields.first { 
            AccessFlags.STATIC.isSet(it.accessFlags) && it.type == statusEnumClass.type
        }

        // B. Find the method 'mo53916e' (getModuleStatus)
        // Identify by signature: takes 1 param, returns the StatusEnum
        val targetMethod = managerClass.methods.first { method ->
            method.parameterTypes.size == 1 &&
            method.returnType == statusEnumClass.type
        }

        // C. Apply the patch
        // Replace method body to return the Available field
        targetMethod.addInstructions(
            0,
            """
                sget-object v0, ${availableField.definingClass}->${availableField.name}:${availableField.type}
                return-object v0
            """
        )
    }
}
