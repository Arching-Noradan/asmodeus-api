/*
 * Copyright (C) 2020, Maksandra (https://github.com/Maksandra)
 *
 * This license allows any person with a copy of the software
 * (hereinafter "SOFTWARE") to use the SOFTWARE without
 * limitations, subject to the following conditions:
 *
 * 1. Any commercial use of the SOFTWARE, any modification
 * thereof (the source code or parts of the source code) is
 * prohibited without written consent from the copyright holder.
 *
 * 2. Usage of any parts of the source code or the whole source
 * code of the SOFTWARE without any copyright credit is prohibited.
 *
 * 3. Each product containing parts of the source code or the
 * source code of the software must inherit this license and
 * therefore comply with the terms and conditions described
 * above and with the disclaimer below.
 *
 * The copyright holder has no right to limit or restrain your
 * permissions until you comply with all the conditions described above.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package space.technological;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

public class DatabaseUtils {
    public static Object getFirstIterable(FindIterable<Document> iterable, Class clas) {
        Object object = null;
        Gson json = new GsonBuilder().create();
        MongoCursor<Document> mongoCursor = iterable.iterator();
        if (mongoCursor.hasNext()) {
            try {
                object = json.fromJson(mongoCursor.next().toJson(), clas);
            } catch (Exception a) {
                a.printStackTrace();
            }
        }
        return object;
    }
}
