The direction of the **magnetic force** is at **right angles** to the plane containing the magnetic field and to the **velocity of a moving charge**.

The force itself is **proportional** to:
- the velocity of the charge, $v$
- the magnitude of the charge, $q$
- the magnetic field strength, $B$

The **magnitude** of magnetic force $F$ is given by $F = qvB\sin(\theta)$ where $\theta$ is the angle between the **velocity of the charge** and the **magnetic field**.
$\therefore F = qvB \implies \sin(\theta) = 1 \implies \theta = 90^\circ$.

**[[magnetic fields|Magnetic field]] strength** $B$ is measured in tesla ($\text{T}$).

There is another way to express $F$; in a conductor that carries an electric current $I$ of charge carriers.

$F=qvB\sin(\theta) \equiv BIL\sin(\theta)$, where $\theta$ would be defined as the angle between the magnetic field and the direction of the current. $L$ is the length of the conductor.

---
Regarding **Fleming's left-hand rule,**

Thumb: Force
Index finger: Magnetic field
Middle finger: Conventional current ^b2e4a4

Remember: **F**ather, **M**other **C**hild

---

$\odot$ illustrates a current, or field direction, coming out of the paper
$\otimes$ illustrates a current, or field direction, going into the paper

Magnetic force $F$ acting on one wire (call it wire $\alpha$) due to the magnetic field of another wire (call it wire $\beta$) must be:
$$
\begin{aligned}
	F = (\text{magnetic field due to wire $\beta$ at the position of wire $\alpha$}) \times I_\alpha \times L_\alpha
\end{aligned}
$$
Therefore, the **force per unit length** acting on wire $\alpha$ is:
$$
\begin{equation}
\frac{F}{L_\alpha} = B\times I_\alpha 
\end{equation}
$$
The magnetic field due to a current at a perpendicular distance $r$ from a wire carrying current $I$ is given by: $B = \mu_0 \frac{I}{2\pi r}$.

Hence, for the case of wires $\alpha$ and $\beta$ here, the force per unit length acting on wire $\alpha$ due to the magnetic field of wire $\beta$ is:
$$
\begin{aligned}
	\frac{F}{L_\alpha} &= (\mu_0\frac{I_\alpha}{2\pi r})\times I_\beta\\
	&=\mu_0\frac{I_\alpha I_\beta}{2\pi r}
\end{aligned}
$$
*note that this is not substituting $B$ in the previous equation!*
The expression above is **symmetrical**, showing that the magnitude of the force of wire $\beta$ on wire $\alpha$ is equal to that of wire $\alpha$ on wire $\beta$. This is supported by Newton's third law.

---
**Regarding charged particles in a uniform [[electric fields|electric field]],**

![[Pasted image 20250817171328.png]]

Since $F=q\times E$, the force on the charge is $Ee$ and the acceleration is $a=\frac{Ee}{m_e}$ because $F=ma$; $m_e$ is the mass of the electron.

The [[electric fields|electric field]] $E$ is produced by the [[electric potential|potential]] difference $V$ between the plates, which are separated by a distance $d$, so $E=\frac{V}{d}$.

And so,
$$
\begin{aligned}
	a &= \frac{Ee}{m_e}\\
	E &= \frac{V}{d}\\
	\therefore a &= \frac{V}{d}\times \frac{e}{m_e}\\
	&=\frac{eV}{m_e\space d}
\end{aligned}
$$
*This is not found in the formula booklet so good luck rofl xD*
In any practical situation, weight can be ignored because the $\text{electric force} \gg \text{weight}$.

---

*In my nightmares*, $\exists$ a charge particle of mass $m$ that enters a uniform magnetic field of strength $B$ perpendicular to the field lines. The charge on the particle is $q$.

![[Pasted image 20250817184415.png]]

The magnetic force always acts at $90^\circ$ to the velocity of the particle. **This is the condition for a centripetal force**, so the particle moves in a circular path. *Insert lightning strike. DUUN DUN DUUUUUUN sound effect.* The speed of the particle is invariant.

Hence, this (kind of) relates to [[orbits in gravitational fields]].

The force on the particle is $qvB$, which must equal $\frac{mv^2}{r}$, where $r$ is the radius of the orbit. The **radius of the orbit** can be expressed in several ways:
$$
r=\frac{mv}{qB}=\frac{p}{qB}=\frac{\sqrt{2mE_k}}{qB}
$$
The derivation for $r=\frac{mv}{qB}$ is as follows:
$$
\begin{aligned}
	qvB &= \frac{mv^2}{r}\\
	qB &= \frac{mv}{r}\\
	r\times qB &= mv\\
	r &= \frac{mv}{qB}\\
	&\qquad\quad QED //
\end{aligned}
$$
The derivation for $r=\frac{p}{qB}$ is as follows:
$$
\begin{aligned}
	qvB &= \frac{mv^2}{r}\\
	r &= \frac{mv}{qB} \qquad\text{(as shown previously)}\\
	p &=mv\\
	\therefore r &= \frac{p}{mv}\\
	&\qquad\quad QED //
\end{aligned}
$$
Note that $p$ is the momentum of the charged particle.

The derivation for $r=\frac{\sqrt{2mE_k}}{qB}$ is as follows:
$$
\begin{aligned}
	qvB &= \frac{mv^2}{r}\\
	r &= \frac{mv^2}{qvB}\\
	r &= \frac{pv}{qvB}\\
	r &= \frac{p}{qB}\\\\
	E_k &= \frac{p^2}{2m} \qquad\text{(according to formula bookie)}\\
	p^2 &= E_k\times 2m\\
	p &= \sqrt{2mE_k}\\\\
	\therefore r &= \frac{\sqrt{2mE_k}}{qB}\qquad\text{(through substitution)}\\
	&\qquad\qquad\quad QED //
\end{aligned}
$$
---

Consider the movement of [[charges|charged particles]] in perpendicular electric and magnetic fields. An example is shown below.

![[Pasted image 20250818101819.png]]

The force as a result of the [[electric fields|electric field]] $E$ is $F_E$. It's direction is due to the fact that like charges repel and opposing charges attract.

Using [[#^b2e4a4|Fleming's left-hand rule]], the force that acts on the charge as a result of the magnetic field $B$, $F_B$ has a direction going downwards.

In the example image above, $F_E$ and $F_B$ act on the charged particle with the same magnitude, where there will be no resultant force; this assumes that the gravitational force is negligible.

And so, for this condition,
$$
\begin{aligned}
	F_E &= F_B\\
	qE &= qvB\\
	E &= vB\\
	v &= \frac{E}{B}\\
\end{aligned}
$$
$v$ would be the single speed at which the particle will travel **without deflection**. At all other speeds, the particle will be deflected upwards (at speeds less than $v\implies F_E > F_B$) or downwards (at speeds greater than $v \implies F_E < F_B$).

This is useful as a velocity **filter** to select specific particles for an experiment.