Rockets and satellites that orbit a planet or star have both KE and GPE when in orbit.

$$
\therefore \text{Total energy of an orbiting satellite} = E = E_p + E_k
$$

To find the expression for $E$ from first principals, express $E_p$ and $E_k$ separately.
Let $M$ be the mass of the planet and $m$ be the mass of the orbiting object (satellite).

Evaluating $E_p$:
$$
\begin{aligned}
	\text{From A.2,}\quad \Delta E_p &= mg\Delta h\\
	E_p &= mgr\\
	&= m\times (-G\frac{M}{r^2})\times r\\
	&= -G\frac{Mm}{r}\\
	&\equiv -G\frac{m_1m_2}{r} \quad \text{as given in formula book}
\end{aligned}
$$

Before evaluating $E_k$, express centripetal force:
$$
\begin{aligned}
	F_{\text{cent}} &= ma_{\text{cent}}\\
	a_{\text{cent}} &= \frac{v^2}{r}\\
	\therefore F_{\text{cent}} &= \frac{mv^2}{r}
\end{aligned}
$$
Now express $E_k$:
$$
\begin{aligned}
	\text{grav. force} &= \text{centripetal force}\\
	g &= F_{\text{cent}}\\
	\therefore G\frac{Mm}{r^2} &= \frac{mv^2}{r}\\
	\frac{1}{2} \times G\frac{Mm}{r^2} &= \frac{1}{2} \times \frac{mv^2}{r}\\
	G\frac{Mm}{2r^2} &= \frac{mv^2}{2r}\\
	G\frac{Mm}{2r} &= \frac{1}{2}mv^2\\
	\therefore E_k &= G\frac{Mm}{2r}
\end{aligned}
$$

Since $E=E_p + E_k$:
$$
\begin{aligned}
	E &= (-G\frac{Mm}{r})+(G\frac{Mm}{2r})\\
	&= -G\frac{Mm}{2r}
\end{aligned}
$$
If the above is mathematically unclear, let variable $u=G\frac{Mm}{r}$ and notice $E=-u+\frac{u}{2}$.

**Yes, this means that $E$ is negative.**
$\implies$ the satellite is bound in its orbit and cannot escape from the planet.

Since $E_k=-\frac{E_p}{2}$, when the satellite moves to a lower orbit, $r$ decreases
$\implies$ $E_k$ increases, but $E_p$ decreases 2 times more (increase in the negative direction).

The expression for $E_k$ can be used to derive $v_{orbital}$ in the formula book, which is a slightly different way to the derivation [[Kepler's laws |here]].

The **orbital speed** of the satellite increases as the orbital radius decreases even though it has lost energy (overall). This is because the total energy becomes more negative — that is, more tightly bound to the system.

*erm, acktually. what about drag?*

A satellite in a low Earth orbit is **subject to drag** caused by **collisions with the ions and molecules in the atmosphere** $\implies \exists$ a drag force in a direction at a **tangent** to the direction of motion of the satellite, in the **opposite direction** to the **linear velocity** of the satellite.

This atmospheric drag removes energy from the Earth-satellite system. Recall,
$$
E=-G\frac{Mm}{2r}
$$
$\therefore E$ decreases $\implies$ $r$ decreases $\implies$ $E_k$ increases $\implies$ $v_{\text{orbital}}$ increases

This whole discussion of drag is true $\iff$ the drag force is small.

If the drag force is large, the **deceleration of the satellite** will be so great and the $E_k$ will be lost so quickly that the satellite **cannot be treated as being in orbit.** It now behaves like an intercontinental ballistic missile or an unidentified flying object — it's a **projectile** subject to Earth's gravity.

---
**Escape speed** of a system: the minimum speed required for an object to leave a gravitational field and (just) reach infinity $\implies$ PE = 0.

Recall that $E_p = -G\frac{Mm}{r}$.

At escape speed, initial $E_k$ must be equal to $|E_p|$ at the point where the satellite is in the field, at distance $r$ from the centre of the sphere that is causing the gravitational field.

$$
\begin{aligned}
	E_k &= |E_p|\\
	\frac{1}{2}{mv_{\text{esc}}}^2 &= G\frac{Mm}{r}\\
	\therefore
	v_{\text{esc}} &= \sqrt{\frac{2 G M}{r}}\\
\end{aligned}
$$
With regards to $v_{\text{orbital}} \iff$ **at the planet surface**,
$$
	v_{\text{esc}}=\sqrt{2} \times v_{\text{orbital}}
$$
The relationship is true if, and only if, the body is at the planet surface as we are dealing with **initial** kinetic energy.

---

**Geosynchronous orbit**: an orbit with an orbital period that matches the Earth's rotation on its axis.

**Geostationary orbit**: An orbit that is geosynchronous and also **positioned above the equator.** It remains apparently fixed in position when viewed from Earth.

---

