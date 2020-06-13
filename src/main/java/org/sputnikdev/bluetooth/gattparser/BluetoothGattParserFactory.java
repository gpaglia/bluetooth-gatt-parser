package org.sputnikdev.bluetooth.gattparser;

/*-
 * #%L
 * org.sputnikdev:bluetooth-gatt-parser
 * %%
 * Copyright (C) 2017 Sputnik Dev
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.sputnikdev.bluetooth.gattparser.num.FloatingPointNumberFormatter;
import org.sputnikdev.bluetooth.gattparser.num.IEEE11073FloatingPointNumberFormatter;
import org.sputnikdev.bluetooth.gattparser.num.IEEE754FloatingPointNumberFormatter;
import org.sputnikdev.bluetooth.gattparser.num.RealNumberFormatter;
import org.sputnikdev.bluetooth.gattparser.num.TwosComplementNumberFormatter;
import org.sputnikdev.bluetooth.gattparser.spec.BluetoothGattSpecificationReader;

/**
 * A factory class for some main objects in the library:
 * {@link BluetoothGattParser}, {@link BluetoothGattSpecificationReader}.
 *
 * @author Vlad Kolotov
 */
public final class BluetoothGattParserFactory {

    private BluetoothGattParserFactory() { }


    /**
     * Returns Bluetooth GATT parser.
     * @return Bluetooth GATT parser
     */
    public static BluetoothGattParser getDefault() {

        return GattParserConfigurationBuilder
            .builder()
            .setDefaults()
            .toConfiguration()
            .getGattParser();
    }

}
