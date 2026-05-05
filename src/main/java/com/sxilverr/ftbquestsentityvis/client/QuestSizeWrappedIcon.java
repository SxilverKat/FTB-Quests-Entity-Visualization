package com.sxilverr.ftbquestsentityvis.client;

import dev.ftb.mods.ftblibrary.icon.Icon;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class QuestSizeWrappedIcon extends Icon {
    private final Icon delegate;
    private final float questSize;

    public QuestSizeWrappedIcon(Icon delegate, float questSize) {
        this.delegate = delegate;
        this.questSize = questSize;
    }

    @Override
    public void draw(GuiGraphics graphics, int x, int y, int w, int h) {
        QuestSizeContext.push(questSize);
        try {
            delegate.draw(graphics, x, y, w, h);
        } finally {
            QuestSizeContext.pop();
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof QuestSizeWrappedIcon other
                && Float.compare(other.questSize, questSize) == 0
                && other.delegate.equals(delegate);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode() * 31 + Float.hashCode(questSize);
    }
}
