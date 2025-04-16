// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.interfaces.logging;

import java.util.logging.Level;

public interface ILogColorProvider {

    String getColor(Level pLevel);
}
